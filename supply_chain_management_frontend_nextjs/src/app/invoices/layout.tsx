'use client';

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Navbar from "../navbar/navbar";
import { userAuth } from "../components/UserAuth";
import GetAllInvoicesByEmployee from '../components/GetAllInvoicesByEmployee'
import AssignInvoice from "../components/AssignInvoice";

export default function InvoicesPageLayout({
    children,
}:{
    children: React.ReactNode;
}){
    const [ isLoggedIn, setIsLoggedIn ] = useState(false);
    const router = useRouter();

    useEffect(() => {
        const getData = async () => {
            const { user, error } = await userAuth();

            //unsuccesful authorization, redirect back to login page
            if(error){
                router.push("/")
                return
            }

            //customer logged in; return to home
            if(user.type == "customer"){
                router.push("/home")
                return
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
    )} else {
        //employee page
        return (
            <main className="background">
                <Navbar></Navbar>
                <div className="page-container">
                    {children}
                    <GetAllInvoicesByEmployee></GetAllInvoicesByEmployee>
                    <AssignInvoice></AssignInvoice>
                </div>
            </main>
    )}
}