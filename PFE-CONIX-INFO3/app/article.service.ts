import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { ARTICLES } from './mock.article';
import { Article } from './article';
import { Configuration } from './configuration';

import 'rxjs/add/operator/toPromise';


@Injectable()
export class ArticleService {
/* TODO: Make the Service available */
	constructor(private configuration : Configuration, private http : Http) { }

	extract( query : string) {
		let restCallUrl: string = this.configuration.ServerWithApiUrl + query;
		return this.http.get(restCallUrl).toPromise(); 
	}

	getArticles() : Promise<Article[]> {
		return Promise.resolve(ARTICLES);
	}

	getArticlesStatic() : Article[] {
		return ARTICLES;
	}

	getArticlePromise() : Promise<Article> {
		return Promise.resolve(ARTICLES[1]);
	}
}