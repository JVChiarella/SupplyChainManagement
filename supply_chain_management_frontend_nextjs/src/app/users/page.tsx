import Navbar from "../navbar/navbar";
import GetAllEmployees from "../components/GetAllEmployees";
import GetAllCustomers from "../components/GetAllCustomers";
import AddEmployee from "../components/AddEmployee";
import AddCustomer from "../components/AddCustomer";
import PatchCustomer from "../components/PatchCustomer";
import PatchEmployee from "../components/PatchEmployee";

const UsersPage = () => {
  return (
    <div>
      <Navbar></Navbar>
      
      <GetAllEmployees></GetAllEmployees>
      <PatchEmployee></PatchEmployee>
      <AddEmployee></AddEmployee>

      <div>-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</div>

      <GetAllCustomers></GetAllCustomers>
      <PatchCustomer></PatchCustomer>
      <AddCustomer></AddCustomer>

      <div>-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</div>
    </div>
  )
}

export default UsersPage