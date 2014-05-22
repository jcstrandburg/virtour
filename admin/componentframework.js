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
	//this.target.val( this.target.val() + text + '\n');
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
}

ComponentFramework.prototype.renderJSON = function() {

    var results = [];
	for ( var i = 0; i < this.components.length; i++) {
		results.push( this.components[i].getJSONObject());
	}

    return JSON.stringify( results);
}

ComponentFramework.prototype.loadFromJSON = function( json) {

    comps = JSON.parse( json);

    this.components = [];

    for ( var i = 0; i < comps.length; i++) {
        logger.log( comps[i].type + " " + comps[i].content);
        switch ( comps[i].type) {
            case 'text':
                logger.log( "found a text object");
                this.components.push( new TextComponent( i, comps[i]));
                break;
            case 'image':
                logger.log( "found an image object");
                this.components.push( new ImageComponent( i, comps[i]));
                break;
            case 'video':
                logger.log( "found a video");
                this.components.push( new VideoComponent( i, comps[i]));
                break;
        }
    }

    this.renderToContainer();
}

ComponentFramework.prototype.addComponent = function(proto) {

	var comp = new proto( this.components.length);
	this.components.push( comp);
    this.renderToContainer();
}


//Component
function Component(index) {
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

//TextComponent Class
inherits( TextComponent, Component);
function TextComponent(index, data) {
	Component.call(this, index);

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
           "Title: <input type='text' name='title' onChange='framework.updateComponent("+this.index+")' value='"+this.title+"'><br>"+
           "Content: <br>"+
           "<textarea name='content' onChange='framework.updateComponent("+this.index+")'>"+this.content+"</textarea>";
}


//ImageComponent Class
inherits( ImageComponent, Component);
function ImageComponent( index, data) {
    Component.call( this, index);

    //check for initializer object, else default initialize
    if (data) {
    }
    else {
    }
}

ImageComponent.prototype.getJSONObject = function() {
    return {type: 'image', url: 'have a url'};
}

ImageComponent.prototype.updateFromDOM = function(source) {
}

ImageComponent.prototype.renderInnerHTML = function() {
	return "ImageComp: "+this.index;
}

//VideoComponent Class
inherits( VideoComponent, Component);
function VideoComponent( index, data) {
    Component.call( this, index);

    //check for initializer object, else default initialize
    if (data) {
    }
    else {
    }
}

VideoComponent.prototype.getJSONObject = function() {
    return {type: 'video', url: 'have a url'};
}
VideoComponent.prototype.updateFromDOM = function(source) {
}

VideoComponent.prototype.renderInnerHTML = function() {
	return "VideoComp: "+this.index;
}

var framework;
var logger;

$(document).ready( function() {
    $("textarea[name=jsontarget]").val("");
	logger = new Logger( $('textarea[name=logbox]'));
	framework = new ComponentFramework( "compContainer");	
});
