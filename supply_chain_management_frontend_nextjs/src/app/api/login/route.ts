import { COOKIE_NAME } from "@/app/constants";
import jwt from "jsonwebtoken";
import { serialize } from "cookie";
import { NextResponse } from "next/server";

interface Credentials {
    username: any;
    password: any; 
    userType: String;
}

export async function POST(request : Request) {
    const credentials : Credentials = await request.json();

    const res = await fetch(`http://localhost:8080/login/${credentials.userType}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username: credentials?.username, 
                           password: credentials?.password  
            }),
    });
    console.log(res)
    const jwt = await res.json();

    if(jwt['token'] === undefined){
        //invalid login
        return NextResponse.json(
            {
                message: "Unauthorizaed",
            },
            {
                status: 401,
            }
        );
    }

    //verifyJWT(jwt['token']);
    
    const serialized = serialize(COOKIE_NAME, jwt['token'], {
        httpOnly: true,
        sameSite: 'strict',
        path: "/"
    });

    const response = {
        message: "user authenitcated"
    };

    return new Response(JSON.stringify(response), {
        status: 200,
        headers: { "Set-Cookie": serialized }
    })
}

function verifyJWT(token : any){
    let authentication = true;
    const secret = process.env.SECRET_KEY || "";
    
    //cant get verification to work. skipping for now; jwt verification works on backend
    /*
    jwt.verify(token, secret, {algorithms : ['HS256'] },
    (err : any, decoded : any) => {
        if (err){
            console.log("error: " + err);
            authentication = false;
        } else {
            console.log(decoded);
            authentication = true;
        }
    });

   try{
        jwt.verify(token, secret, {algorithms : ['HS256'] });
        console.log("jwt verification success")
   }catch(e){
        authentication = false;
        console.log("jwt verification failed")
   }
   */

    return authentication;
}