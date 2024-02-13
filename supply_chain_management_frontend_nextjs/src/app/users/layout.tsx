'use client';

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { userAuth } from "../components/UserAuth";

export default function UsersPageLayout({
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