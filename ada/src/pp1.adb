with Ada.Text_IO; use Ada.Text_IO;
with Obj_Package; use Obj_Package;

procedure Pp1 is
   task type Add_Chunk_Task is
      entry Calculate (
         ChunkStart : Integer;
         ChunkEnd : Integer;
         Length : Integer
      );
      entry Finish_Calculate;
   end Add_Chunk_Task;

   task body Add_Chunk_Task is
   begin
         accept Calculate (
            ChunkStart : Integer;
            ChunkEnd : Integer;
            Length : Integer
            ) do
            for I in ChunkStart .. ChunkEnd loop
               Set (Get (I) + Get (Length - I - 1), I);
            end loop;
         end Calculate;

         accept Finish_Calculate;
   end Add_Chunk_Task;

   -- God bless rizorko for these two rows
   type WorkerPtr is access all Add_Chunk_Task;
   type WorkerPrtArr is array (Integer range 0 .. 9) of WorkerPtr;

   Tasks_Array : WorkerPrtArr;
   LeftoversTask: WorkerPtr;

   Tasks_Per_Thread : Integer;
   Wave : Integer := 1;
   Half : Integer;
   Leftovers: Integer;

begin
   loop
      exit when Get_Length = 1;
      Half := Get_Length / 2;

      for I in 0 .. 9 loop
         Tasks_Array (I) := new Add_Chunk_Task;
      end loop;

      Tasks_Per_Thread := Half / 10;
      Leftovers := Half mod 10;

      if Leftovers /= 0 then
         LeftoversTask := new Add_Chunk_Task;
      end if;

      Put_Line ("Wave = " &
         Integer'Image (Wave) &
         " with array length = " &
         Integer'Image (Get_Length) &
         ", half = " &
         Integer'Image (Half) &
         " and leftovers  = " &
         Integer'Image (Leftovers) 
      );

      if Tasks_Per_Thread /= 0 then
         for I in 0 .. 9 loop
            Tasks_Array (I).Calculate ( 
               I * Tasks_Per_Thread,
               I * Tasks_Per_Thread + Tasks_Per_Thread - 1,
               Get_Length
            );
         end loop;
         for I in 0 .. 9 loop
            Tasks_Array (I).Finish_Calculate;
         end loop;
      end if;

      if Leftovers /= 0 then
         LeftoversTask .Calculate (
            Half - Leftovers,
            Half - 1,
            Get_Length
         );
      end if;

      Set_Length (Get_Length / 2 + Get_Length mod 2);
      Wave := Wave + 1;
   end loop;

   Put_Line (Integer'Image (Get (0)));
end Pp1;