'use client';

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Navbar from "../navbar/navbar";
import AddStock from "../components/AddStock";
import { userAuth } from "../components/UserAuth";
import DeleteStock from "../components/DeleteStock";
import PatchStock from "../components/PatchStock";
import GetAllStock from "../components/GetAllStock";

export default function StockPageLayout({
    children,
}:{
    children: React.ReactNode;
}){
    const [ isLoggedIn, setIsLoggedIn ] = useState(false);
    const router = useRouter();
    const [ isCustomer, setIsCustomer ] = useState(false);
    const [ isAdmin, setIsAdmin ] = useState(false);

    //state to pass to children to reload table on changes
    const [ updateFetch, setUpdateFetch ] = useState(false);

    useEffect(() => {
        const getData = async () => {
            const { user, error } = await userAuth();

            //unsuccesful authorization, redirect back to login page
            if(error){
                router.push("/")
                return
            }

            //customer logged in; save in state
            if(user.type == "customer"){
                setIsCustomer(true);
            } else {
                //check admin status of employee
                const admin = await getAdmin();
                if(admin){
                    setIsAdmin(true);
                }
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
    )} else if(isCustomer){
        //customer page
        return (
            <main className="background">
                <Navbar type="customer"></Navbar>
                <div className="page-container">
                    {children}
                    <GetAllStock updateFetch={updateFetch} setUpdateFetch={setUpdateFetch}></GetAllStock>
                </div>
            </main>
    )} else if(isAdmin){ 
        //admin employee page
        return (
            <main className="background-large">
                <Navbar></Navbar>
                <div className="page-container">
                    {children}
                    <GetAllStock updateFetch={updateFetch} setUpdateFetch={setUpdateFetch}></GetAllStock>
                    <AddStock setUpdateFetch={setUpdateFetch}></AddStock>
                    <PatchStock setUpdateFetch={setUpdateFetch}></PatchStock>
                    <DeleteStock setUpdateFetch={setUpdateFetch}></DeleteStock>
                </div>
            </main>
    )} else { 
        //basic employee page
        return(
        <main className="background">
            <Navbar></Navbar>
            <div className="page-container">
                {children}
                <GetAllStock updateFetch={updateFetch} setUpdateFetch={setUpdateFetch}></GetAllStock>
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