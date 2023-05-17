import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface City {
  id?: number;
  name: string;
  pictureUrl: string
}

export interface CitiesPage {
  cities: City[];
  page: number;
  totalPages: number;
}

@Injectable({
  providedIn: 'root'
})
export class CityService {

  cities: City[] = [];

  private baseUrl = 'http://localhost:8080/cities';

  constructor(private http: HttpClient) {
  }

  public getCitiesPaginated(params: any): Observable<any> {
    return this.http.get(`${this.baseUrl}` + '/page', {params});
  }

  public getCity(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}` + '/' + id);
  }

  public updateCity(id: number, city: City): Observable<any> {
    return this.http.put(`${this.baseUrl}` + '/' + id, city);
  }

}
