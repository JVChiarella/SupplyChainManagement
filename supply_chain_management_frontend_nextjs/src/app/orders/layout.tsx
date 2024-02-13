'use client';

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Navbar from "../navbar/navbar";

export default function HomePageLayout({
    children,
}:{
    children: React.ReactNode;
}){
    const [ isLoggedIn, setIsLoggedIn ] = useState(false);
    const router = useRouter();
    const [ isCustomer, setIsCustomer ] = useState(false);

    useEffect(() => {
        const getData = async () => {
            const { user, error } = await getUser();

            //unsuccesful authorization, redirect back to login page
            if(error){
                router.push("/")
                return
            }

            //redirect employee to home
            if(user.type == "employee"){
                router.push("/home");
                return
            }

            //customer logged in; save in state
            if(user.type == "customer"){
                setIsCustomer(true);
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
        //only customers can go to orders page
        if(isCustomer){
            return (
                <main>
                    <Navbar type="customer"></Navbar>
                    {children}
                </main>
        )}
    }
}

async function getUser() {
    const res = await fetch('/api/auth', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
    });
    
    //if auth api returns anything but success, throw error and redirect to login
    if(res.statusText != "OK"){
        return {
            user: null,
            error: "Unauthorized"
        }
    }

    const data = await res.json();
    return {
        user: data,
        error: null,
    };
}