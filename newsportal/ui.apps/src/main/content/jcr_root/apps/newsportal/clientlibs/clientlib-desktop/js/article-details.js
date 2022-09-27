$(function() {


    $.ajax({
        url: "/newsportal/services/recent-articles-qb",
        type: 'GET',
        dataType: 'json', // added data type
        success: function(res) {
            debugger;
            for(var article in res) {
                if(article !== "undefined" && article[article] !== "undefined") {
					var data = res[article];
                    var html = "<li><a href='"+data.path+"'>"+data.title+"</a></li>"
                    $(html).prependTo("#recent-articles-list");
                }
            }    
        }
    });


    var ajaxurl = location.pathname;
    ajaxurl = ajaxurl.replace(".html","/jcr:content");

    $.ajax({
        url: ajaxurl+".incategory.json",
        type: 'GET',
        dataType: 'json', // added data type
        success: function(res) {
            for(var article in res) {
                if(article !== "undefined" && article[article] !== "undefined") {
					var data = res[article];
                    var html = "<div class='nl-item'><div class='nl-img'><img src='"+data.image+"'></div><div class='nl-title'><a href='"+data.path+"'>"+data.title+"</a></div></div>"
                    $(html).prependTo("#in-this-category");
                }
            }    
        }
    });


     $.ajax({
        url: ajaxurl+".relatedarticles.json",
        type: 'GET',
        dataType: 'json', // added data type
        success: function(res) {
            for(var article in res) {
                if(article !== "undefined" && article[article] !== "undefined") {
					var data = res[article];
					var html = "<li><a href='"+data.path+"'>"+data.title+"</a></li>"                    
         			$(html).prependTo("#related-article-pages");
                }
            }    
        }
    });






});