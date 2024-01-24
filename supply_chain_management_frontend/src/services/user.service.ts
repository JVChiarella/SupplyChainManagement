import { Injectable } from '@angular/core';
import fetchFromAPI from './api';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorService } from './error.service';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from './auth.service';

const DEFAULT_EMPLOYEE : Employee = {
  credentials : {
    username : "",
    password : "",
  },
  firstName : "",
  lastName : "",
  email : "",
  active : false,
  admin : false,
}

const DEFAULT_CUSTOMER : Customer = {
  credentials : {
    username : "",
    password : ""
  },
  firstName : "",
  lastName : "",
  active : false,
  email : "",
  address : "",
  phoneNumber : ""
}

@Injectable({
  providedIn: 'root'
})

export class UserService {

  employee : Employee = DEFAULT_EMPLOYEE;
  customer : Customer = DEFAULT_CUSTOMER;
  username : string = "";
  password : string = "";
  admin : boolean = false;

  constructor(private errorService: ErrorService, private cookieService: CookieService) {}

  async getAllCustomers(): Promise<Customer[]> {
    const endpoint = 'customers';
    const response = await fetchFromAPI('GET', endpoint);
    return response;
  }

  async getCustomer(id : number): Promise<Customer[]> {
    const endpoint = `customers/${id}`;
    const response = await fetchFromAPI('GET', endpoint);
    return response;
  }

  async addCustomer(customer : Customer) {
    const endpoint = `employees/add`;
    await fetchFromAPI('POST', endpoint, customer);
  }

  async getAllEmployee(): Promise<Employee[]> {
    const endpoint = 'employees';
    const response = await fetchFromAPI('GET', endpoint);
    return response;
  }

  async getEmployee(id : number): Promise<Employee[]> {
    const endpoint = `employees/${id}`;
    const response = await fetchFromAPI('GET', endpoint);
    return response;
  }

  async addEmployee(employee : Employee) {
    const endpoint = `employees/add`;
    await fetchFromAPI('POST', endpoint, employee);
  }
  
  private handleHttpError(error: HttpErrorResponse) {
    if (error.status === 403) {
      // Handle rate limit exceeded error
      const customError = {
        error: 'API rate limit exceeded',
        tips: 'Please try again later.',
      };
      this.errorService.setError(customError);
    } else if (error.status === 404) {
      // Handle not found error
      const customError = {
        error: 'Not found',
        tips: 'The user was not found.',
      };
      this.errorService.setError(customError);
    } else {
      // Handle other HTTP errors
      const customError = {
        error: 'An error occurred',
        tips: 'Please try again later.',
      };
      this.errorService.setError(customError);
    }
  }
}