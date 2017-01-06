import { Component } from '@angular/core';
import { OnInit } from '@angular/core';

import { Article } from './article';
import { ArticleService } from './article.service';
import { ArticleDetailComponent } from './article.detail';

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
        this.articleService.extract(this.searchEntry).then(r => r.json()).then(r => this.articles = r);
    }
}