import { Component, Input } from '@angular/core';
import { OnInit } from '@angular/core';

import { Article } from './article';

@Component({
    moduleId : module.id,
    selector: 'article-detail',
    templateUrl: './article.detail.html',   
    styleUrls: [ 'article.detail.css' ],
})
export class ArticleDetailComponent {
	@Input()
	article : Article;

	hide() : void {
		this.article = null;
	}
}