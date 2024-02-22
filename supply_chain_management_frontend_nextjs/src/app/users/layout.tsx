'use client';

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { userAuth } from "../components/UserAuth";
import Navbar from "../navbar/navbar";
import GetAllCustomers from "../components/GetAllCustomers";
import AddEmployee from "../components/AddEmployee";
import AddCustomer from "../components/AddCustomer";
import PatchCustomer from "../components/PatchCustomer";
import PatchEmployee from "../components/PatchEmployee";
import DeleteCustomer from "../components/DeleteCustomer";
import DeleteEmployee from "../components/DeleteEmployee";
import GetAllEmployees from "../components/GetAllEmployees";

export default function UsersPageLayout({
    children,
}:{
    children: React.ReactNode;
}){
    const [ isLoggedIn, setIsLoggedIn ] = useState(false);
    const [ isAdmin, setIsAdmin ] = useState(false);
    const router = useRouter();

    //state to pass to children to reload tables on change
    const [ updateEmployeeFetch, setUpdateEmployeeFetch ] = useState(false);
    const [ updateCustomerFetch, setUpdateCustomerFetch ] = useState(false);

    useEffect(() => {
        const getData = async () => {
            const { user, error } = await userAuth();

            //unsuccesful authorization, redirect back to login page
            if(error){
                router.push("/")
                return
            }

            //customer logged in; redirect to home
            if(user.type == "customer"){
                router.push("/home")
                return
            }

            //check admin status
            const admin = await getAdmin();
            if(admin){
                setIsAdmin(true);
            }
            
            //successful login
            setIsLoggedIn(true);
        };
        getData();
    }, [router]);

    if(!isLoggedIn){
        return (
        <p>
            Loading...
        </p>
    )} else if(isAdmin){ 
        return (
            <main className="background-large">
                <Navbar></Navbar>
                <div className="page-container">
                    {children}
                    <GetAllEmployees updateEmployeeFetch={updateEmployeeFetch} setUpdateEmployeeFetch={setUpdateEmployeeFetch}></GetAllEmployees>
                    <AddEmployee setUpdateEmployeeFetch={setUpdateEmployeeFetch}></AddEmployee>
                    <PatchEmployee setUpdateEmployeeFetch={setUpdateEmployeeFetch}></PatchEmployee>
                    <DeleteEmployee setUpdateEmployeeFetch={setUpdateEmployeeFetch}></DeleteEmployee>

                    <GetAllCustomers updateCustomerFetch={updateCustomerFetch} setUpdateCustomerFetch={setUpdateCustomerFetch}></GetAllCustomers>
                    <AddCustomer setUpdateCustomerFetch={setUpdateCustomerFetch}></AddCustomer>
                    <PatchCustomer setUpdateCustomerFetch={setUpdateCustomerFetch}></PatchCustomer>
                    <DeleteCustomer setUpdateCustomerFetch={setUpdateCustomerFetch}></DeleteCustomer>
                </div>
            </main>
    )} else { 
        return (
            <main className="background">
                <Navbar></Navbar>
                <div className="page-container">
                    {children}
                    <GetAllEmployees updateEmployeeFetch={updateEmployeeFetch} setUpdateEmployeeFetch={setUpdateEmployeeFetch}></GetAllEmployees>
                </div>
            </main>
    )}
}

async function getAdmin(){
    //get admin status from employee token
    const res1 = await fetch('/api/getUserInfo', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    });

    const data = await res1.json();

    return data.admin;
}