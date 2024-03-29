'use client';

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Navbar from "../navbar/navbar";
import { userAuth } from "../components/UserAuth";

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
            const { user, error } = await userAuth();

            //unsuccesful authorization, redirect back to login page
            if(error){
                router.push("/")
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
        //customer home page
        if(isCustomer){
            return (
                <main className = "background">
                    <Navbar type="customer"></Navbar>
                    {children}
                </main>
        )} else {
        //employee home page
        return (
            <main className = "background">
                <Navbar></Navbar>
                {children}
            </main>
        )}
    }
}