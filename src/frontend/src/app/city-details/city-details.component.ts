import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {CityService} from "../service/city.service";
import {ActivatedRoute, Router} from "@angular/router";


@Component({
  selector: 'app-city-details',
  templateUrl: './city-details.component.html',
  styleUrls: ['./city-details.component.scss']
})
export class CityDetailsComponent implements OnInit {

  id: number = 753;
  cityName: string = '';
  pictureUrl: string = '';
  error: string = '';

  form: FormGroup = new FormGroup({
    cityName: new FormControl(this.cityName, [Validators.required, Validators.maxLength(50)]),
    pictureUrl: new FormControl(this.pictureUrl, [Validators.required, Validators.maxLength(200)])
  });

  constructor(private service: CityService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    this.getCityDetailById();
  }

  getCityDetailById() {
    this.service.getCity(this.id).subscribe((res: any) => {
      this.id = res.id;
      this.cityName = res.name;
      this.pictureUrl = res.pictureUrl;

      this.form.patchValue({
        cityName: this.cityName,
        pictureUrl: this.pictureUrl
      })
    });
  }

  save() {
    const formData = {...this.form.value};

    this.service.updateCity(this.id, {
      id: this.id,
      name: this.form.controls.cityName.value,
      pictureUrl: this.form.controls.pictureUrl.value
    }).subscribe((res: any) => {
      this.gotoList();
    }, error => {
      this.error = error.error.name;
    })
  }

  cancel() {
    this.form.reset();
    this.gotoList();
  }

  gotoList() {
    this.router.navigate(['/']);
  }
}
