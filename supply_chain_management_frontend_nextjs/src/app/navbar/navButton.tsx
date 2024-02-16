import React from 'react'
import Link from 'next/link'

const NavButton = (props : any) => {
    return (
        <div>
            <button className= "navbar-button">
                <Link href= {"./" + props.name}>{props.name}</Link>
            </button>
        </div>
        )
}

export default NavButton