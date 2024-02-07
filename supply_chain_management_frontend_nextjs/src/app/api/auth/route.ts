import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";
import { NextResponse } from "next/server";

export async function GET(){
    const cookieStore = cookies();

    const token = cookieStore.get(COOKIE_NAME);
    if(token){
        //verify jwt once working

        return new Response("Token Authenticated", {
            status: 200,
        })
    } else {
        return NextResponse.json(
            {
                message: "Unauthorizaed",
            },
            {
                status: 401,
            }
        );
    }
}