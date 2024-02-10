'use client';

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function UsersPageLayout({
    children,
}:{
    children: React.ReactNode;
}){
    const [ isLoggedIn, setIsLoggedIn ] = useState(false);
    const router = useRouter();

    useEffect(() => {
        const getData = async () => {
            const { user, error } = await getUser();

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
    return (
        <main>
            {children}
        </main>
    )}
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