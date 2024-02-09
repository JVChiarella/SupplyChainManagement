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

            //unsuccesful login, redirect back to login page
            if(error){
                router.push("/")
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
    
    //if auth api returns anything but success, redirect user to home
    if(res.statusText != "OK"){
        return {
            user: null,
            error: "Unauthorized"
        }
    }

    const data = res.json();
    return {
        user: data,
        error: null,
    };
}