import { Component } from '@angular/core';
import { OnInit } from '@angular/core';

import { Article } from './article';
import { ArticleService } from './article.service';
import { ArticleDetailComponent } from './article.detail';
declare var $:JQueryStatic;

@Component({
    moduleId : module.id,
    selector: 'my-app',
    templateUrl: 'app.component.html',    
    styleUrls: [ 'app.component.css' ],
})
export class AppComponent {
    title : string = "PFE-INFO3-CONIX";
    articles : Article[];
    restArticles : Article[];
    selectedArticle : Article;
    searchEntry : string;
    loaderStatus : string = "inactive";

    constructor(private articleService : ArticleService){ }

    ngOnInit() : void {
        this.resolveArticles();
    }

    resolveArticles() : void {
        this.articles = this.articleService.getArticlesStatic();  
    }

    onSelect(article : Article){
        this.selectedArticle = article;
    }
    search() : void {
        this.loaderStatus = "active";
        this.articleService.extract(this.searchEntry)
          .then(r => r.json())
          .catch(e => alert("un incident est survenu" + e))
          .then(r => {
              this.articles = r; 
              this.loaderStatus = "inactive"; 
              
              r.forEach(art => 
              {
                    art.negWords.forEach(w => 
                    {
                        $("#negWordsUL").append("<li><a href=\"#\" target=\"_blank\">"+w+"</a></li>");
                    });   
              });

              r.forEach(art => 
              {
                    art.posWords.forEach(w => 
                    {
                        $("#posWordsUL").append("<li><a href=\"#\" target=\"_blank\">"+w+"</a></li>");
                    });   
              });
              
              try {
                  TagCanvas.Start('canvasNeg','negWords',{
                    textColour: '#ED0000',
                    outlineColour: '#ED0000',
                    reverse: true,
                    depth: 0.8,
                    maxSpeed: 0.05
                  });
                  TagCanvas.Start('canvasPos','posWords',{
                      textColour: '#2C75FF',
                      outlineColour: '#2C75FF',
                      reverse: true,
                      depth: 0.8,
                      maxSpeed: 0.05
                    });
                } catch(e) {
                  // something went wrong, hide the canvas container
                    console.log("Une ereur est survenue lors de la création des nuages de mots");
                  document.getElementById('myCanvasContainer').style.display = 'none';
                }
             
          });
        
    }
    

}