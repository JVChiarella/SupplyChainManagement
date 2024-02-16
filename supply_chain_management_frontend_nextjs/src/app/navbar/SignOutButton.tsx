'use client';

import React from 'react';
import Link from 'next/link'
const signOutButton = () => {
    return(
        <div>
            <button className= "navbar-button" onClick={logoutUser}>
                <Link href= {"./"}>sign out</Link>
            </button>
        </div>
    )
}

async function logoutUser(){
    try{
        const res = await fetch('/api/logout', {
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

export default signOutButton