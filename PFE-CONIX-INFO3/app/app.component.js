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
var article_service_1 = require("./article.service");
var AppComponent = (function () {
    function AppComponent(articleService) {
        this.articleService = articleService;
        this.title = "PFE-INFO3-CONIX";
        this.loaderStatus = "inactive";
    }
    AppComponent.prototype.ngOnInit = function () {
        this.resolveArticles();
    };
    AppComponent.prototype.resolveArticles = function () {
        this.articles = this.articleService.getArticlesStatic();
    };
    AppComponent.prototype.onSelect = function (article) {
        this.selectedArticle = article;
    };
    AppComponent.prototype.search = function () {
        var _this = this;
        this.loaderStatus = "active";
        this.articleService.extract(this.searchEntry)
            .then(function (r) { return r.json(); })
            .catch(function (e) { return alert("un incident est survenu" + e); })
            .then(function (r) {
            _this.articles = r;
            _this.loaderStatus = "inactive";
            //                var words = [
            //                  {text: r[2].negWords[0]},
            //                  {text: r[2].negWords[1]},
            //                ];
            var nwords = [];
            r.forEach(function (art) {
                art.negWords.forEach(function (w) {
                    nwords.push({ text: w, weight: 0.3 });
                });
            });
            console.log('words negatifs', nwords);
            $('#negWords').jQCloud(nwords);
            var pwords = [];
            r.forEach(function (art) {
                art.posWords.forEach(function (w) {
                    pwords.push({ text: w, weight: 0.1 });
                });
            });
            console.log('words positifs', pwords);
            $('#posWords').jQCloud(pwords, {
                colors: ["#800026", "#bd0026", "#e31a1c",
                    "#fc4e2a", "#fd8d3c", "#feb24c", "#fed976",
                    "#ffeda0", "#ffffcc"]
            });
        });
    };
    return AppComponent;
}());
AppComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'my-app',
        templateUrl: 'app.component.html',
        styleUrls: ['app.component.css'],
    }),
    __metadata("design:paramtypes", [article_service_1.ArticleService])
], AppComponent);
exports.AppComponent = AppComponent;
//# sourceMappingURL=app.component.js.map