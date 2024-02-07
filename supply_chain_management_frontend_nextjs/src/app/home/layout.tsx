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
        (async () => {
            const { user, error } = await getUser();

            console.log("user");

            if(error){
                router.push("/")
            }
            
            //successful login
            setIsLoggedIn(true);
        });
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
        console.log(data);
        
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