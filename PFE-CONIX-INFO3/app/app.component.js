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
        this.title = "Trouvez la santé de votre entreprise !";
        this.loaderStatus = "inactive";
        this.tagCanvasStatus = "inactive";
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
        $("#bRechercher").prop('disabled', true);
        this.articleService.extract(this.searchEntry)
            .then(function (r) { return r.json(); })
            .catch(function (e) { return alert("un incident est survenu" + e); })
            .then(function (r) {
            _this.articles = r;
            _this.loaderStatus = "inactive";
            $("#bRechercher").prop('disabled', false);
            $("#negWordsUL").children().remove();
            $("#posWordsUL").children().remove();
            var mySetNeg = new Set();
            var mySetPos = new Set();
            r.forEach(function (art) {
                art.negWords.forEach(function (w) {
                    if (mySetNeg.size < 25 && !mySetNeg.has(w)) {
                        mySetNeg.add(w);
                        $("#negWordsUL").append("<li><a href=\"" + art.link + "\" target=\"_blank\" >" + w + "</a></li>");
                    }
                });
                art.posWords.forEach(function (w) {
                    if (mySetPos.size < 25 && !mySetPos.has(w)) {
                        mySetPos.add(w);
                        $("#posWordsUL").append("<li><a href=\"" + art.link + "\" target=\"_blank\" >" + w + "</a></li>");
                    }
                });
            });
            _this.tagCanvasStatus = "active";
            try {
                TagCanvas.Start('canvasNeg', 'negWords', {
                    textColour: '#d9534f',
                    outlineColour: '#d9534f',
                    reverse: true,
                    depth: 0.8,
                    maxSpeed: 0.05
                });
                TagCanvas.Start('canvasPos', 'posWords', {
                    textColour: '#5cb85c',
                    outlineColour: '#5cb85c',
                    reverse: true,
                    depth: 0.8,
                    maxSpeed: 0.05
                });
            }
            catch (e) {
                console.log("Une ereur est survenue lors de la création des nuages de mots");
                document.getElementById('myCanvasContainer').style.display = 'none';
            }
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