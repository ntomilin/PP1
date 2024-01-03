with Integer_Array_Type_Package;

package Obj_Package is
   type Integer_Array_Type is 
      new Integer_Array_Type_Package.Integer_Array_Type;

   procedure Set (Item : Integer; I: Natural);
   function Get (I : Natural) return Integer;

   procedure Set_Length (Length : Integer);
   function Get_Length return Integer;
end Obj_Package;