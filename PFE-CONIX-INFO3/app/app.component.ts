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
    title : string = "Trouvez la santé de votre entreprise !";
    articles : Article[];
    restArticles : Article[];
    selectedArticle : Article;
    searchEntry : string;
    loaderStatus : string = "inactive";
    tagCanvasStatus : string ="inactive"

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
        $("#bRechercher").prop('disabled', true);
        this.articleService.extract(this.searchEntry)
          .then(r => r.json())
          .catch(e => alert("un incident est survenu" + e))
          .then(r => {
              this.articles = r; 
              this.loaderStatus = "inactive";
              $("#bRechercher").prop('disabled', false);
              $("#negWordsUL").children().remove();
              $("#posWordsUL").children().remove();
              var mySetNeg = new Set();
              var mySetPos = new Set();
              r.forEach(art => 
              {
                    art.negWords.forEach(w => 
                    {
                        if(mySetNeg.size < 25 && !mySetNeg.has(w))
                        {
                            mySetNeg.add(w);
                            $("#negWordsUL").append("<li><a href=\""+art.link+"\" target=\"_blank\" >"+w+"</a></li>");
                        }
                        
                    });
                    
                    art.posWords.forEach(w => 
                    {
                        if(mySetPos.size < 25 && !mySetPos.has(w))
                        {
                            mySetPos.add(w);
                            $("#posWordsUL").append("<li><a href=\""+art.link+"\" target=\"_blank\" >"+w+"</a></li>");
                        }
                    });   
               });
             
              this.tagCanvasStatus = "active";
              
              try {
                  TagCanvas.Start('canvasNeg','negWords',{
                    textColour: '#d9534f',
                    outlineColour: '#d9534f',
                    reverse: true,
                    depth: 0.8,
                    maxSpeed: 0.05
                  });
                  TagCanvas.Start('canvasPos','posWords',{
                      textColour: '#5cb85c',
                      outlineColour: '#5cb85c',
                      reverse: true,
                      depth: 0.8,
                      maxSpeed: 0.05
                    });
                } catch(e) 
                {
                    console.log("Une ereur est survenue lors de la création des nuages de mots");
                  document.getElementById('myCanvasContainer').style.display = 'none';
                }
             
          });
        
    }
    

}