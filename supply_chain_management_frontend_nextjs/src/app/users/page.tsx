import Navbar from "../navbar/navbar";
import GetAllEmployees from "../components/GetAllEmployees";
import GetAllCustomers from "../components/GetAllCustomers";
import AddEmployee from "../components/AddEmployee";
import AddCustomer from "../components/AddCustomer";

const UsersPage = () => {
  return (
    <div>
      <Navbar></Navbar>
      
      <GetAllEmployees></GetAllEmployees>
      <AddEmployee></AddEmployee>

      <div>-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</div>

      <GetAllCustomers></GetAllCustomers>
      <AddCustomer></AddCustomer>

      <div>-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</div>
    </div>
  )
}

export default UsersPage