'use client';

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function HomePageLayout({
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
    try{
        const res = await fetch('/api/auth', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        const data = res.json();

        return {
            user: data,
            error: null,
        };
    } catch (e){
        return {
            user: null,
            error: e,
        }
    }
}