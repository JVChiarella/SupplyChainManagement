'use client';
import React from 'react';
import Link from 'next/link'

const signOutButton = () => {
    //remove jwt and return to home
    
    return(
        <div className= "navbar-button">
            <Link href= {"./"}>sign out</Link>
        </div>
    )
}

export default signOutButton