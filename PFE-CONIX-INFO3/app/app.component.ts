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
//                var words = [
//                  {text: r[2].negWords[0]},
//                  {text: r[2].negWords[1]},
//                ];
              var nwords = [];
              r.forEach(art => 
              {
                    art.negWords.forEach(w => 
                    {
                        nwords.push({text:w, weight:0.3});    
                    });   
              });
              console.log('words negatifs', nwords); 
              $('#negWords').jQCloud(nwords);
              
              var pwords = [];
              r.forEach(art => 
              {
                    art.posWords.forEach(w => 
                    {
                        pwords.push({text:w, weight:0.1});    
                    });   
              });
              console.log('words positifs', pwords);
              $('#posWords').jQCloud(pwords,
                  {
                      colors: ["#800026", "#bd0026", "#e31a1c",
                  "#fc4e2a", "#fd8d3c", "#feb24c", "#fed976", 
                  "#ffeda0", "#ffffcc"] 
                  }
              );
          })
            
                        
          ;
    }
}