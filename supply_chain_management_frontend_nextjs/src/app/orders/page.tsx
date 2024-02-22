'use client'
import React, { useState } from 'react'
import GetAllOrdersByCustomer from '../components/GetAllOrdersByCustomer'
import AddOrder from '../components/AddOrder'

const OrdersPage = () => {
  const [ updateFetch, setUpdateFetch ] = useState(false);
  return (
    <div>
        <GetAllOrdersByCustomer updateFetch={updateFetch} setUpdateFetch={setUpdateFetch}></GetAllOrdersByCustomer>
        <AddOrder setUpdateFetch={setUpdateFetch}></AddOrder>
    </div>
  )
}

export default OrdersPage