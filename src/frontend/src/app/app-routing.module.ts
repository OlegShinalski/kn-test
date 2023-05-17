import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {CitiesListComponent} from "./cities-list/cities-list.component";
import {CityDetailsComponent} from "./city-details/city-details.component";

const routes: Routes = [
  {path: '', component: CitiesListComponent},
  {path: 'list', component: CitiesListComponent},
  {path: 'details/:id', component: CityDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule {

}
