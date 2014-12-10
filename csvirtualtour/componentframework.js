var resourceIndexLocation = "media/list.php";

//class inheritance helper function
function inherits(subClass, baseClass) {

    function F() {
    }
    F.prototype = baseClass.prototype;
    subClass.prototype = new F();
    subClass.prototype.constructor = subClass;
    subClass.baseConstructor = baseClass;
    subClass.superClass = baseClass.prototype;
}


//Logger Class
function Logger(target) {
	this.target = target;
    this.target.val('Debugging log\n');
}

Logger.prototype.log = function(text) {
	this.target.val( this.target.val() + text + '\n');
}


//ComponentFramework Class
function ComponentFramework(container) {
	this.container = $('div[name='+container+']');

	this.components = []
}

ComponentFramework.prototype.moveComponent = function( index, direction) {
    //find the index of the component to be swapped with and make sure its valid
    target = index+direction;
    if (target < 0 || target >= this.components.length) {
        return;
    }

    //change indexes
    this.components[index].changeIndex( target);
    this.components[target].changeIndex( index);

    //swap
    temp = this.components[index];
    this.components[index] = this.components[target];
    this.components[target] = temp;

    //redraw
    this.redrawComponent( index);
    this.redrawComponent( target);
}

ComponentFramework.prototype.redrawComponent = function( index) {
    $("div#comp"+index).replaceWith( this.components[index].renderHTML());
	this.components[index].applyEventHandlers();
}

ComponentFramework.prototype.deleteComponent = function( index) {
    logger.log( "Removing "+index);

    this.components.splice( index, 1);

    for ( var i = index; i < this.components.length; i++) {
        this.components[i].changeIndex( i);
    }

    this.renderToContainer();
}

ComponentFramework.prototype.updateComponent = function( index) {
    logger.log( "Updating "+index);

    var div = $("div#comp"+index);
    this.components[index].updateFromDOM( div);
}

ComponentFramework.prototype.renderHTML = function() {

	html = "";
	for ( var i = 0; i < this.components.length; i++) {
		html += this.components[i].renderHTML();
	}

	return html;
}

ComponentFramework.prototype.renderToContainer = function() {
	this.container.html( this.renderHTML());
	for ( var i = 0; i < this.components.length; i++) {
		this.components[i].applyEventHandlers();
	}
}

ComponentFramework.prototype.renderJSON = function() {

    var results = [];
	for ( var i = 0; i < this.components.length; i++) {
		results.push( this.components[i].getJSONObject());
	}

    json = JSON.stringify( results);
    return json;
}

ComponentFramework.prototype.loadFromJSON = function( json) {

    comps = JSON.parse( json);

    this.components = [];

    for ( var i = 0; i < comps.length; i++) {
        switch ( comps[i].type) {
            case 'text':
                logger.log( "found a text object");
                this.components.push( new TextComponent(this, i, comps[i]));
                break;
            case 'image':
                logger.log( "found an image object");
                this.components.push( new ImageComponent(this, i, comps[i]));
                break;
            case 'video':
                logger.log( "found a video");
                this.components.push( new VideoComponent(this, i, comps[i]));
                break;
        }
    }

    this.renderToContainer();
}

ComponentFramework.prototype.addComponent = function(proto) {

	var comp = new proto(this, this.components.length);
	this.components.push( comp);
    this.renderToContainer();
}


//Component
function Component(parent, index) {
	this.parent = parent;
	this.index = index;
}

Component.prototype.index = -1;

Component.prototype.renderHTML = function() {
	
	return this.renderFrame( this.renderInnerHTML());
}

Component.prototype.updateFromDOM = function(source) {
    logger.log( "updateFromDOM not implemented");
}

Component.prototype.changeIndex = function(index) {
    this.index = index;
}

Component.prototype.getJSONObject = function() {
    return {type: 'generic'};
}

Component.prototype.renderFrame = function(inner) {
    return "<div class='component' id='comp"+this.index+"'>"+
           "<div class='componentSection'>"+
           "<button onClick='framework.moveComponent("+this.index+", -1)'>Move Up</button><br>"+
           "<button onClick='framework.moveComponent("+this.index+", 1)'>Move Down</button><br>"+
           "<button onClick='framework.deleteComponent("+this.index+")'>Delete</button><br>"+
           "</div><div class='componentSection'>"+
           inner+
           "</div></div>";
}

Component.prototype.renderInnerHTML = function() {
	return "Generic Component: "+this.index;
}

Component.prototype.applyEventHandlers = function() {
	var index = this.index;
	$("#comp"+index).find("input,textarea").change(function(){framework.updateComponent(index);});
}

//TextComponent Class
inherits( TextComponent, Component);
function TextComponent(parent, index, data) {
	Component.call(this, parent, index);

    //check for initializer object, else default initialize
    if (data) {
        this.title = data.title;
        this.content = data.content;
    }
    else {
        this.title = "Title";
        this.content = "Content";
    }
}

TextComponent.prototype.getJSONObject = function() {
    return {type: 'text', title: this.title, content: this.content};
}

TextComponent.prototype.updateFromDOM = function(source) {

    this.title = source.find("input[name=title]").val();
    this.content = source.find("textarea[name=content]").val();
}

TextComponent.prototype.renderInnerHTML = function() {

    return "<h4>Text Component</h4>"+
           "Title: <input type='text' name='title' value='"+this.title+"'><br>"+
           "Content: <br>"+
           "<textarea name='content'>"+this.content+"</textarea>";
}

TextComponent.prototype.applyEventHandlers = function() {
	Component.prototype.applyEventHandlers.call(this);
}


//ImageComponent Class
inherits( ImageComponent, Component);
function ImageComponent( parent, index, data) {
    Component.call( this, parent, index);

    //check for initializer object, else default initialize
    if (data) {
		this.url = data.url;
		this.title = data.title;
    }
    else {
		this.title = "";
		this.url = "";
    }
}

ImageComponent.prototype.getJSONObject = function() {
    return {type: 'image', title: this.title, url: this.url};
}

function url_correct(url) {

    url = url.replace(/\s/g, "%20");

    if (url.indexOf("http://") != 0) {
    	return "http://"+url;
    }
    else {
        return url;
    }
}

ImageComponent.prototype.updateFromDOM = function(source) {
    this.title = source.find("input[name=title]").val();
    this.url = url_correct(source.find("input[name=url]").val());
}

ImageComponent.prototype.renderInnerHTML = function() {
    return "<h4>Image Component</h4>"+
           "Title: <input type='text' name='title' value='"+this.title+"'><br>"+
           "URL: <input type='text' name='url' value='"+this.url+"'><br>"+
		   "<button class='fileSelectButton'>Select Media From Server</button>"+
		   "</div><div class='previewPane'>"+
           "<img class='largePreview' alt='Preview' src='"+this.url+"'><br>";
}

ImageComponent.prototype.applyEventHandlers = function() {
	Component.prototype.applyEventHandlers.call(this);
	var container = $("#comp"+this.index);
	
	container.find("img.largePreview").error(function(){
		$(this).attr('src', 'error.png');
	});
	
	container.find("button.fileSelectButton").click(function(){
		DoFileSelect("image", function(filepath){
			container.find('input[name=url]').val(filepath).change();
		});
	});
	
	container.find("input[name=url]").change(function(){
		url = $(this).val();
		container.find("img.largePreview").attr('src', url);
	});
	
	img = container.find("img.largePreview");
	img.attr('src', img.attr('src'));
}

//VideoComponent Class
inherits( VideoComponent, Component);
function VideoComponent( parent, index, data) {
    Component.call( this, parent, index);

    //check for initializer object, else default initialize
    if (data) {
		this.url = data.url;
		this.title = data.title;
    }
    else {
		this.title = "";
		this.url = "";
    }
}

VideoComponent.prototype.getJSONObject = function() {
    return {type: 'video', title: this.title, url: this.url};
}
VideoComponent.prototype.updateFromDOM = function(source) {
    this.title = source.find("input[name=title]").val();
    this.url = url_correct(source.find("input[name=url]").val());
}

VideoComponent.prototype.renderInnerHTML = function() {
    return "<h4>Video Component</h4>"+
           "Title: <input type='text' name='title' value='"+this.title+"'><br>"+
           "URL: <input type='text' name='url' value='"+this.url+"'><br>"+
		   "<button class='fileSelectButton' type='button'>Select Media From Server</button>"+
		   "</div><div class='previewPane'>"+
           "Preview: <video alt='preview' src='"+this.url+"' controls></video><br>";
}

VideoComponent.prototype.applyEventHandlers = function() {
	Component.prototype.applyEventHandlers.call(this);
	var container = $("#comp"+this.index);
	
	container.find("button.fileSelectButton").click(function(){
		DoFileSelect("video", function(filepath){
			container.find('input[name=url]').val(filepath).change();
		});
	});	
	
	container.find("input[name=url]").change(function(){
		url = $(this).val();
		container.find("video").attr('src', url);
	});
}

function DoFileSelect(type, callback) {

	var filters;
	if (type == 'image') {
		filters = "png,jpg";
	}
	else if (type == 'video') {
		filters = "mp4,ogg";	
	}

	html = "<div class='fileSelectWrapper'><div class='fileSelectInner'><h3>Select File (formats: "+filters+")</h3><select name='url' id='mediaSelector' size=20></select><br><button id='doFileSelect'>Select File</button><button id='cancelFileSelect'>Cancel</button></div></div>"
	$("body").append(html);
	
	$.ajax({
		url: resourceIndexLocation,
		type: "POST",
		dataType: "json",
		data: {"filters": filters},
		
	}).done(function(data){
		options = $('#mediaSelector');
		for(var elem in data) {
			options.append("<option value='"+data[elem]+"'>"+elem+"</option>");
		}
	});
	
	$("#doFileSelect").click(function(){
		callback($('#mediaSelector').val());
		$(".fileSelectWrapper").remove();		
	});
	$("#cancelFileSelect").click(function(){
		$(".fileSelectWrapper").remove();
	});
}

var framework;
var logger;

$(document).ready(function(){
	logger = new Logger( $('textarea[name=logbox]'));
});
