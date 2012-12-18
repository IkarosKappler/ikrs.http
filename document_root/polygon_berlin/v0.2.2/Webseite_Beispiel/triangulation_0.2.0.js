/**
 * @see http://www.travellermap.com/tmp/delaunay.htm
 *
 * @date 2012-10-17
 **/


//--- BEGIN ------------------------ Constants ---------------------------------
/**
 * Use absolute or relative path??!
 **/
var backgroundTextureAddress = "Unknown.png"; // "file:///C:/Users/Lea%20Skywalker/Downloads/ikaros/Webseite_Beispiel/Unknown.png";

var paintEdges             = false;
var fillTriangles          = true;
var paintPoints            = false;
var paintTriangles         = true;
var paintBackgroundTexture = true;
var useAdditionalBoxNodes  = false;
//--- END -------------------------- Constants ---------------------------------



var backgroundTexture;
var polyCanvas;
var context;         
var vertexSet;

/**
 * A box array for storing the tiles in PolyBox objects.
 **/
var boxes;



/**
 * This function has to be called to init the canvas data!
 *
 * It adds a 'resize' listener to the current window.
 **/
function polyInit() {
    
    polyCanvas   = document.getElementById("polyCanvas");
    context      = polyCanvas.getContext("2d");

    window.addEventListener('resize', resizeCanvas, false);
    resizeCanvas();
}


function resizeCanvas() {
    polyCanvas.width = window.innerWidth;
    polyCanvas.height = getDocumentHeight();
    
    /**
     * Your drawings need to be inside this function otherwise they will be reset when 
     * you resize the browser window and the canvas goes will be cleared.
     */
    performPolygonTriangulation(); 
}

function performPolygonTriangulation() {
    paintBackground(); 
    buildPolyBoxes();
    buildPointSet(); 

    if( !paintBackgroundTexture && paintTriangles )
	generateTriangles(); 

    paintBoxes();

    if( paintPoints ) 
	paintPoints();
}

/** 
 * @see http://james.padolsey.com/javascript/get-document-height-cross-browser/ 
 **/
function getDocumentHeight() {
    var D = document;
    return Math.max(
        Math.max(D.body.scrollHeight, D.documentElement.scrollHeight),
        Math.max(D.body.offsetHeight, D.documentElement.offsetHeight),
        Math.max(D.body.clientHeight, D.documentElement.clientHeight)
    );
}
    

function PolyBox( x , y, w, h ) {
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;
}

function Point2D( x, y ) {
    this.x = x;
    this.y = y;
}

function paintBackground() {

    if( !paintBackgroundTexture ) {

	// Paint a boder around the canvas?
	context.fillStyle = "rgb(128,128,128)"; 
	context.strokeStyle = "rgb(0,0,0)";
	context.lineWidth = 2;
	
	context.fillRect( 0, 0, polyCanvas.width, polyCanvas.height );

    } else {
	
	
	backgroundTexture = new Image();
	backgroundTexture.onload = function(){

	    var pattern = context.createPattern(backgroundTexture, "repeat");
	    
	    //console.log( "Painting texture ... " );
	    //console.log( backgroundTexture );	
	    
	    context.fillStyle = pattern;
	    context.fillRect( 0, 0, polyCanvas.width, polyCanvas.height );


	    if( paintTriangles )
		generateTriangles();
	    
	};
	backgroundTexture.src = backgroundTextureAddress;

    } // END else

}

function buildPolyBoxes() {

    boxes = new Array(  
		      );
    // Add test boxes?
    /*
      boxes[ 0 ] = new PolyBox( 20, 20, 120, 120 ),
      boxes[ 1 ] = new PolyBox( 160, 20, 80, 80 ),
      boxes[ 2 ] = new PolyBox( 260, 20, 80, 80 ),
      boxes[ 3 ] = new PolyBox( 10, 160, 120, 120 ),
      boxes[ 4 ] = new PolyBox( 180, 160, 240, 240 )
    */
		        

    // Ad all sections (CSS class='box') to the box list.
    var i = boxes.length;    
    $(".box").each(function(index, el) {
	    // Position des Targets
	    var tmp = $(el);
	    //console.log( tmp );
	    var coords = tmp.offset();	
	    //console.log( coords );
	    var left   = coords.left;
	    var top    = coords.top;
	    var width  = tmp.width();
	    var height = tmp.height();
	 
	    //console.log( "top=" + top + ", left=" + left + ", width=" + width + ", height=" + height );

	    boxes[ i++ ] = new PolyBox( left, top, width, height );
	    
	});

}



function paintBoxes() {
    
    // Paint a boder around the canvas
    // context.strokeStyle = "rgb(0,0,0)";
    context.lineWidth = 2;
    
    //context.fillRect( 1, 1, polyCanvas.width-2, polyCanvas.height-2 );
    
    context.fillStyle = "rgb(0,0,0)";
    for( var i = 0; i < boxes.length; i++ ) {
        
        
        var box = boxes[i];
        
        context.fillRect( box.x, box.y, box.width, box.height );
        
    }
}

function buildPointSet() {
    
    this.vertexSet = new Array(); //  boxes.length * 4 );
    // Build a bounding point set.
    this.vertexSet[0] = new Point2D( 0, 0 );
    this.vertexSet[1] = new Point2D( polyCanvas.width, 0 );
    this.vertexSet[2] = new Point2D( polyCanvas.width, polyCanvas.height );
    this.vertexSet[3] = new Point2D( 0, polyCanvas.height );

    
    // Add more points to the canvas borders to get a more beautiful triangulation.
    var avrgBorderPointDistance = 100;
    for( var h = 0; h < (polyCanvas.width / avrgBorderPointDistance); h++ ) {

	this.vertexSet[ vertexSet.length ] = new Point2D( Math.random() * this.polyCanvas.width, 0 );
	this.vertexSet[ vertexSet.length ] = new Point2D( Math.random() * this.polyCanvas.width, this.polyCanvas.height );
    }
    for( var v = 0; v < (polyCanvas.height / avrgBorderPointDistance); v++ ) {

	this.vertexSet[ vertexSet.length ] = new Point2D( 0, Math.random() * this.polyCanvas.height );
	this.vertexSet[ vertexSet.length ] = new Point2D( polyCanvas.width, Math.random() * this.polyCanvas.height );
    }
    

    // Convert boxes to 2D point plane
    var leadingPointCount = vertexSet.length;
    for( var i = 0; i < boxes.length; i++ ) {
	
	var box = boxes[ i ];
	
	this.vertexSet[ vertexSet.length ]     = new Point2D( box.x, box.y );
	this.vertexSet[ vertexSet.length ] = new Point2D( box.x + box.width, box.y );
	this.vertexSet[ vertexSet.length ] = new Point2D( box.x, box.y + box.height );
	this.vertexSet[ vertexSet.length ] = new Point2D( box.x + box.width, box.y + box.height );

	
	// Add additional nodes?
	if( useAdditionalBoxNodes ) {
	    this.vertexSet[ vertexSet.length ] = new Point2D( box.x + box.width/2, box.y );
	    this.vertexSet[ vertexSet.length ] = new Point2D( box.x + box.width, box.y + box.height/2 );
	    this.vertexSet[ vertexSet.length ] = new Point2D( box.x + box.width/2, box.y + box.height );
	    this.vertexSet[ vertexSet.length ] = new Point2D( box.x + box.width/2, box.y + box.height/2 );
	}
	
    } 
}

function paintPoints() {

    // alert( pointSet );
    
    //context.strokeStyle = "rgb(255,0,0)";
    context.fillStyle = "#FF0000";
    context.lineWidth = 4;
    for( i in this.vertexSet ) {

        
        var point = this.vertexSet[ i ];

	context.beginPath();
	context.arc( point.x, point.y, 3, 0, Math.PI*2, true );
	context.closePath();

	
	context.fill();
        
        
    }
    
}


function generateTriangles() {

    // context.fillStyle = "rgb(200,200,200)";
    context.strokeStyle = "rgb(0,0,255)";

    var triangles = Triangulate( this.vertexSet );
    // alert( triangles );
    
    var i = 0;
    for( i in triangles ) {

	var tri = triangles[i];
	//alert( tri );
	if( !tri )
	    continue;


	
	var r, g, b;

	// Any RGB color?
	r = Math.round(Math.random()*255);
	g = Math.round(Math.random()*255);
	b = Math.round(Math.random()*255);
	
	g = b = r;
	
	// A dark/bright contrast?
	/*var dark = (Math.random() >= 0.5);
	if( dark ) {
	    r = g = b = Math.round(Math.random()*92);
	} else {
	    r = g = b = 192 + Math.round(Math.random()*(255-192));
	}
	*/

	var alpha = 0.15; // Math.random()*0.25;

	
	context.fillStyle = "rgba(" + r + ", " + g + ", " + b + ", " + alpha + ")"; 

	// Draw the triangle's outer path
	context.beginPath();				
        context.moveTo( tri.v0.x, tri.v0.y );
        context.lineTo( tri.v1.x, tri.v1.y );
	context.lineTo( tri.v2.x, tri.v2.y );
        context.fill();

	if( paintEdges )
	    context.stroke();
    }

}


/* Validated with JSLint, http://www.jslint.com */


////////////////////////////////////////////////////////////////////////////////
//
// Delaunay Triangulation Code, by Joshua Bell
//
// Inspired by: http://www.codeguru.com/cpp/data/mfc_database/misc/article.php/c8901/
//
// This work is hereby released into the Public Domain. To view a copy of the public 
// domain dedication, visit http://creativecommons.org/licenses/publicdomain/ or send 
// a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, 
// California, 94105, USA.
//
////////////////////////////////////////////////////////////////////////////////


var EPSILON = 1.0e-6;

//------------------------------------------------------------
// Vertex class
//------------------------------------------------------------

function Vertex( x, y )
{
	this.x = x;
	this.y = y;
	
} // Vertex

//------------------------------------------------------------
// Triangle class
//------------------------------------------------------------

function Triangle( v0, v1, v2 )
{
	this.v0 = v0;
	this.v1 = v1;
	this.v2 = v2;

	this.CalcCircumcircle();
	
} // Triangle

Triangle.prototype.CalcCircumcircle = function()
{
	// From: http://www.exaflop.org/docs/cgafaq/cga1.html

	var A = this.v1.x - this.v0.x; 
	var B = this.v1.y - this.v0.y; 
	var C = this.v2.x - this.v0.x; 
	var D = this.v2.y - this.v0.y; 

	var E = A*(this.v0.x + this.v1.x) + B*(this.v0.y + this.v1.y); 
	var F = C*(this.v0.x + this.v2.x) + D*(this.v0.y + this.v2.y); 

	var G = 2.0*(A*(this.v2.y - this.v1.y)-B*(this.v2.x - this.v1.x)); 
	
	var dx, dy;
	
	if( Math.abs(G) < EPSILON )
	{
		// Collinear - find extremes and use the midpoint

		function max3( a, b, c ) { return ( a >= b && a >= c ) ? a : ( b >= a && b >= c ) ? b : c; }
		function min3( a, b, c ) { return ( a <= b && a <= c ) ? a : ( b <= a && b <= c ) ? b : c; }

		var minx = min3( this.v0.x, this.v1.x, this.v2.x );
		var miny = min3( this.v0.y, this.v1.y, this.v2.y );
		var maxx = max3( this.v0.x, this.v1.x, this.v2.x );
		var maxy = max3( this.v0.y, this.v1.y, this.v2.y );

		this.center = new Vertex( ( minx + maxx ) / 2, ( miny + maxy ) / 2 );

		dx = this.center.x - minx;
		dy = this.center.y - miny;
	}
	else
	{
		var cx = (D*E - B*F) / G; 
		var cy = (A*F - C*E) / G;

		this.center = new Vertex( cx, cy );

		dx = this.center.x - this.v0.x;
		dy = this.center.y - this.v0.y;
	}

	this.radius_squared = dx * dx + dy * dy;
	this.radius = Math.sqrt( this.radius_squared );
}; // CalcCircumcircle

Triangle.prototype.InCircumcircle = function( v )
{
	var dx = this.center.x - v.x;
	var dy = this.center.y - v.y;
	var dist_squared = dx * dx + dy * dy;

	return ( dist_squared <= this.radius_squared );
	
}; // InCircumcircle


//------------------------------------------------------------
// Edge class
//------------------------------------------------------------

function Edge( v0, v1 )
{
	this.v0 = v0;
	this.v1 = v1;
	
} // Edge


//------------------------------------------------------------
// Triangulate
//
// Perform the Delaunay Triangulation of a set of vertices.
//
// vertices: Array of Vertex objects
//
// returns: Array of Triangles
//------------------------------------------------------------
function Triangulate( vertices )
{
	var triangles = [];

	//
	// First, create a "supertriangle" that bounds all vertices
	//
	var st = CreateBoundingTriangle( vertices );

	triangles.push( st );

	//
	// Next, begin the triangulation one vertex at a time
	//
	var i;
	for( i in vertices )
	{
		// NOTE: This is O(n^2) - can be optimized by sorting vertices
		// along the x-axis and only considering triangles that have 
		// potentially overlapping circumcircles

		var vertex = vertices[i];
		AddVertex( vertex, triangles );
	}

	//
	// Remove triangles that shared edges with "supertriangle"
	//
	for( i in triangles )
	{
		var triangle = triangles[i];

		if( triangle.v0 == st.v0 || triangle.v0 == st.v1 || triangle.v0 == st.v2 ||
			triangle.v1 == st.v0 || triangle.v1 == st.v1 || triangle.v1 == st.v2 ||
			triangle.v2 == st.v0 || triangle.v2 == st.v1 || triangle.v2 == st.v2 )
		{
			delete triangles[i];
		}
	}

	return triangles;
	
} // Triangulate


// Internal: create a triangle that bounds the given vertices, with room to spare
function CreateBoundingTriangle( vertices )
{
	// NOTE: There's a bit of a heuristic here. If the bounding triangle 
	// is too large and you see overflow/underflow errors. If it is too small 
	// you end up with a non-convex hull.
	
	var minx, miny, maxx, maxy;
	for( var i in vertices )
	{
		var vertex = vertices[i];
		if( minx === undefined || vertex.x < minx ) { minx = vertex.x; }
		if( miny === undefined || vertex.y < miny ) { miny = vertex.y; }
		if( maxx === undefined || vertex.x > maxx ) { maxx = vertex.x; }
		if( maxy === undefined || vertex.y > maxy ) { maxy = vertex.y; }
	}

	var dx = ( maxx - minx ) * 10;
	var dy = ( maxy - miny ) * 10;
	
	var stv0 = new Vertex( minx - dx,   miny - dy*3 );
	var stv1 = new Vertex( minx - dx,   maxy + dy   );
	var stv2 = new Vertex( maxx + dx*3, maxy + dy   );

	return new Triangle( stv0, stv1, stv2 );
	
} // CreateBoundingTriangle


// Internal: update triangulation with a vertex 
function AddVertex( vertex, triangles )
{
	var edges = [];
	
	// Remove triangles with circumcircles containing the vertex
	var i;
	for( i in triangles )
	{
		var triangle = triangles[i];

		if( triangle.InCircumcircle( vertex ) )
		{
			edges.push( new Edge( triangle.v0, triangle.v1 ) );
			edges.push( new Edge( triangle.v1, triangle.v2 ) );
			edges.push( new Edge( triangle.v2, triangle.v0 ) );

			delete triangles[i];
		}
	}

	edges = UniqueEdges( edges );

	// Create new triangles from the unique edges and new vertex
	for( i in edges )
	{
		var edge = edges[i];

		triangles.push( new Triangle( edge.v0, edge.v1, vertex ) );
	}	
} // AddVertex


// Internal: remove duplicate edges from an array
function UniqueEdges( edges )
{
	// TODO: This is O(n^2), make it O(n) with a hash or some such
	var uniqueEdges = [];
	for( var i in edges )
	{
		var edge1 = edges[i];
		var unique = true;

		for( var j in edges )
		{
			if( i != j )
			{
				var edge2 = edges[j];

				if( ( edge1.v0 == edge2.v0 && edge1.v1 == edge2.v1 ) ||
					( edge1.v0 == edge2.v1 && edge1.v1 == edge2.v0 ) )
				{
					unique = false;
					break;
				}
			}
		}
		
		if( unique )
		{
			uniqueEdges.push( edge1 );
		}
	}

	return uniqueEdges;
	
} // UniqueEdges



// Henning Diesenberg, 2012-10-17
window.setTimeout( "polyInit();", 1000 );


