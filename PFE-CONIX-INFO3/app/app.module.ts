import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent }  from './app.component';
import { ArticleService } from './article.service';
import { Configuration } from './configuration';
import { ArticleDetailComponent } from './article.detail';

@NgModule({
	imports: [
    	BrowserModule,
    	FormsModule,
		HttpModule
  	],
  	declarations: [ 
  		AppComponent, 
  		ArticleDetailComponent, 
  	],
  	providers: [ 
  		ArticleService, 
  		Configuration 
  	],
  	bootstrap: [ AppComponent ]
})
export class AppModule { }