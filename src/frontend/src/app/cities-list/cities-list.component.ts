import {Component, OnInit} from '@angular/core';
import {CityService} from '../service/city.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-cities-list',
  templateUrl: './cities-list.component.html',
  styleUrls: ['./cities-list.component.scss']
})
export class CitiesListComponent implements OnInit {

  name: string = '';
  public cities = [];
  page: number = 1;
  pageSize: number = 9;
  totalItems: number = 0;

  constructor(private service: CityService, private router: Router) {
    console.log('Application loaded. Initializing data.');
  }

  ngOnInit() {
    this.getCities(1);
  }

  getCities(pageNo: number): void {
    this.page = pageNo;
    const params = this.getRequestParams(this.name.trim(), this.page, this.pageSize);
    this.service.getCitiesPaginated(params).subscribe((res: any) => {
      this.cities = res.content;
      this.page = res.number + 1;
      this.totalItems = res.totalElements;
    });
  }

  getRequestParams(searchTitle: string, page: number, pageSize: number): any {
    let params: any = {};
    if (searchTitle) {
      params[`name`] = searchTitle;
    }
    if (page) {
      params[`page`] = page;
    }
    if (pageSize) {
      params[`pageSize`] = pageSize;
    }
    return params;
  }

  clearSearchField() {
    this.name = '';
  }

  editDetails(id: number) {
    this.router.navigate(['/details', id]);
  }
}
