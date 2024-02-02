import React from 'react'
import Link from 'next/link'

const NavButton = (props : any) => {
    return (
        <div className= "navbar-button">
            <Link href= {"./" + props.name}>{props.name}</Link>
        </div>
        )
}

export default NavButton