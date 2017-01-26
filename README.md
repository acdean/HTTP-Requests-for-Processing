Introduction
------------

HTTP Requests for Processing is a small library that takes the pain out of doing HTTP requests in Processing. 

HTTP Requests for Processing is based on code by [Chris Allick](http://chrisallick.com/) and [Daniel Shiffman](http://www.shiffman.net/).

This fork allows easy posting of json data, see bottom example. This was written in repsonse to this post on the processing forum: https://forum.processing.org/two/discussion/comment/50477

How to
------------
Install the library by [downloading the latest release](https://github.com/runemadsen/HTTProcessing/releases) or via the [Processing contribution manager](http://wiki.processing.org/w/How_to_Install_a_Contributed_Library).

Then import the library in your sketch:
    
    import http.requests.*;

Then you can make GET and POST requests from your code:

    GetRequest get = new GetRequest("http://httprocessing.heroku.com");
    get.send();
    println("Response Content: " + get.getContent());
    println("Response Content-Length Header: " + get.getHeader("Content-Length"));
    
    PostRequest post = new PostRequest("http://httprocessing.heroku.com");
    post.addData("name", "Rune");
    post.send();
    println("Response Content: " + post.getContent());
    println("Response Content-Length Header: " + post.getHeader("Content-Length"));
    

    // now with headers and json support
    PostRequest post = new PostRequest("http://httprocessing.heroku.com");
    post.addHeader("acdHeader", "hello world");
    post.addJson("{\"items\": [{\"checked\": true, \"text\": \"one\"}, {\"checked\": true, \"text\": \"two\"}]}");
    post.send();
    println("Response Content: " + post.getContent());
    println("Response Content-Length Header: " + post.getHeader("Content-Length"));


    // now with binary support
    PostRequest post = new PostRequest("http://httprocessing.heroku.com");
    byte[] bytes = new byte[10];
    for (int i = 0 ; i < byte.length ; i++) {
      bytes[i] = i;
    }
    post.addData("application/octet-stream", bytes);
    post.send();
    println("Response Content: " + post.getContent());
    println("Response Content-Length Header: " + post.getHeader("Content-Length"));
    
    // or from a file
    post.addDataFromFile("image/jpeg", "/full/path/to/file.jpg");


    // PUT support too
    PostRequest put = new PostRequest("http://httprocessing.heroku.com", "ISO-8859-1");
    put.method("PUT");
    ...
    put.send();
    
