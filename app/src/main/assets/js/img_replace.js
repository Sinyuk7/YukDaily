function openImage(url) {
	if(window.injectedObject){
		injectedObject.openImage(url);
	}
}


//function img_replace_by_url(url,cache) {
//
//    var objs = document.getElementsByTagName("img");
//
//    for(var i=0;i<objs.length;i++) {
//        var link = objs[i].getAttribute("link");
//        if(link == url) {
//            objs[i].setAttribute("src",cache);
//            objs[i].setAttribute("onclick","openImage('" + cache + "')");
//         return "succeed: cache: " + cache + "link: " + link;
//        }
//    }
//    return "failed: cache: " + cache + "link: " + link;
//}



function img_replace_all() {

	var result = '';

	var objs = document.getElementsByTagName("img");
	for(var i=0;i<objs.length;i++) {
		var cache = objs[i].getAttribute("cache");
		objs[i].setAttribute("src", cache);
   		objs[i].setAttribute("onclick","openImage('" + cache + "')");

		result = result + cache;
		result = result + ",";
	}

	return result;
}

function img_replace(source, replaceSource) {
    $('img[zhimg-src*="'+source+'"]').each(function () {
        $(this).attr('src', replaceSource);
    });
}

