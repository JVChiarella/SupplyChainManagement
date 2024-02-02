'use client';
import React from 'react';
import { signOut, useSession } from "next-auth/react";

const signOutButton = () => {
    const { data: session } = useSession();
    if(session && session.user){
        return(
            <div className= "navbar-button">
                <button onClick={() => signOut({ callbackUrl: "http://localhost:3000" }) }>
                    Sign Out 
                </button>
            </div>
        )
    }
}

export default signOutButton