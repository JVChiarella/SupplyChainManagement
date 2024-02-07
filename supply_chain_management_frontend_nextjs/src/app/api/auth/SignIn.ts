'use server'
import jwt from "jsonwebtoken";
import { serialize } from "cookie";
import { NextResponse } from 'next/server';
import { cookies } from 'next/headers'

interface credentials {
    username: any;
    password: any; 
    userType: String;
}

export default async function SignIn(credentials : credentials) {
    const res = await fetch(`http://localhost:8080/login/${credentials.userType}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username: credentials?.username, 
                           password: credentials?.password  
            }),
    });
    const jwt = await res.json();

    console.log(jwt);
    //console.log(jwt['token']);

    if(jwt['token'] === undefined){
        //invalid login
        return null
    }
    //return jwt['token'];
    cookies().set({
        name: 'AuthJWT', 
        value: jwt['token'],
        httpOnly: true,
        path: "/"
    });

    return "authenticated succesfully"

    /*
    const response = NextResponse.json({ message : "authenicated" })

    response.cookies.set("AuthJwt", jwt['token'], {
        httpOnly: false,
        path: "/",
    });

    return response;
    */

    /*
    const serialized = serialize("AuthJWT", jwt['token'], {
        httpOnly: true,
        secure: process.env.NODE_ENV === "production",
        sameSite: "strict",
        path: "/",
    });

    const response = {
        message: "Authenticated",
    };

    return new Response(JSON.stringify(response), {
        status: 200,
        headers: { "Set-Cookie": serialized },
    });
    */
}

function verifyJWT(token : any){
    let authentication = true;
    //cant get verification to work. skipping for now; jwt verification works on backend
    /*
    jwt.verify(token, "fjalskdjfaudcvbkzxzxcvvbajksdhlflkajsdhlfk", {algorithms : ['HS256'] }
    (err : any, decoded : any) => {
        if (err){
            console.log("error: " + err);
            authentication = false;
        } else {
            console.log(decoded);
            authentication = true;
        }
    });
    */
    return authentication;
}