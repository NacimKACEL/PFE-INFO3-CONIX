import { Injectable } from '@angular/core';

@Injectable()
export class Configuration {
    public Server: string = "http://localhost:8080/";
    public ApiUrl: string = "SpringMavenHibernate/extract/utf8/";
    public ServerWithApiUrl: string = this.Server + this.ApiUrl;
}