package body Obj_Package is
   protected Obj is
      procedure Set (Item : Integer; I : Natural);
      function Get (I : Natural) return Integer;
      procedure Set_Length (Length : Integer);
      function Get_Length return Integer;
   private
      Data : Integer_Array_Type (0 .. 999) := (others => 1);
      Array_Length : Integer := 1000;
   end Obj;

   protected body Obj is
      procedure Set (Item : Integer; I : Natural) is
      begin
         Data (I) := Item;
      end Set;

      function Get (I : Natural) return Integer is
      begin
         return Data (I);
      end Get;

      procedure Set_Length (Length : Integer) is
      begin
         Array_Length := Length;
      end Set_Length;

      function Get_Length return Integer is
      begin
         return Array_Length;
      end Get_Length;

   end Obj;

   function Get (I : Natural) return Integer is
   begin
      return Obj.Get (I);
   end Get;

   procedure Set (Item : Integer; I : Natural) is
   begin
      Obj.Set (Item, I);
   end Set;

   procedure Set_Length (Length : Integer) is
   begin
      Obj.Set_Length (Length);
   end Set_Length;

   function Get_Length return Integer is
   begin
      return Obj.Get_Length;
   end Get_Length;

end Obj_Package;