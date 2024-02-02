import React from 'react'

const DEFAULT_USER : User = {
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

export default class UserService{
    user : User = DEFAULT_USER;
    customer : Customer = DEFAULT_CUSTOMER;
    employee : Employee = DEFAULT_EMPLOYEE;
    username : string = "";
    password : string = "";
    active : boolean = false;
    admin? : boolean = false;
    firstName : string = "";
    lastName : string = "";
    email : string = "";
    phoneNumber : string = "";

    constructor(){};

    setUser(user: any, username : any, password : any) {
        this.user = user;
        this.username = username;
        this.password = password;
    }

    setEmployee(employee : any, username : any, password : any){
        this.employee = employee;
        this.username = username;
        this.password = password;
    }

    getEmployee(){
        return this.employee;
    }
    
    setCustomer(customer : any, username : any, password : any){
        this.customer = customer;
        this.username = username;
        this.password = password;
    }

    getCustomer(){
        return this.customer;
    }

    getUser(){
        return this.user;
    }

}