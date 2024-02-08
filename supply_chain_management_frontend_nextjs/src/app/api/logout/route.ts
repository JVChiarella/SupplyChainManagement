import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";
import { NextResponse } from "next/server";

export async function GET(){
    const cookieStore = cookies();

    //cookie not found
    if(!cookieStore.has(COOKIE_NAME)){
        return NextResponse.json(
            {
                message: "Unauthorizaed",
            },
            {
                status: 401,
            }
        );
    }

    const token = cookieStore.delete(COOKIE_NAME);
    return new Response("User Logged Out", {
        status: 200,
    });
}