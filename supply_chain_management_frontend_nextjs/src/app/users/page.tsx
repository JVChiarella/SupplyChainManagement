import GetAllEmployees from "../components/GetAllEmployees";
import GetAllCustomers from "../components/GetAllCustomers";
import AddEmployee from "../components/AddEmployee";
import AddCustomer from "../components/AddCustomer";
import PatchCustomer from "../components/PatchCustomer";
import PatchEmployee from "../components/PatchEmployee";
import DeleteCustomer from "../components/DeleteCustomer";
import DeleteEmployee from "../components/DeleteEmployee";

const UsersPage = () => {
  return (
    <div>
      <GetAllEmployees></GetAllEmployees>
      <PatchEmployee></PatchEmployee>
      <AddEmployee></AddEmployee>
      <DeleteEmployee></DeleteEmployee>

      <GetAllCustomers></GetAllCustomers>
      <PatchCustomer></PatchCustomer>
      <AddCustomer></AddCustomer>
      <DeleteCustomer></DeleteCustomer>
    </div>
  )
}

export default UsersPage