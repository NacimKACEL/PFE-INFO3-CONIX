"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var http_1 = require("@angular/http");
var mock_article_1 = require("./mock.article");
var configuration_1 = require("./configuration");
require("rxjs/add/operator/toPromise");
var ArticleService = (function () {
    /* TODO: Make the Service available */
    function ArticleService(configuration, http) {
        this.configuration = configuration;
        this.http = http;
    }
    ArticleService.prototype.extract = function (query) {
        var restCallUrl = this.configuration.ServerWithApiUrl + query;
        return this.http.get(restCallUrl).toPromise();
    };
    ArticleService.prototype.getArticles = function () {
        return Promise.resolve(mock_article_1.ARTICLES);
    };
    ArticleService.prototype.getArticlesStatic = function () {
        return mock_article_1.ARTICLES;
    };
    ArticleService.prototype.getArticlePromise = function () {
        return Promise.resolve(mock_article_1.ARTICLES[1]);
    };
    return ArticleService;
}());
ArticleService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [configuration_1.Configuration, http_1.Http])
], ArticleService);
exports.ArticleService = ArticleService;
//# sourceMappingURL=article.service.js.map