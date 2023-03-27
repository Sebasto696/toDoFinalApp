package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.utils.adapter.ToDoAdapter
import com.example.myapplication.utils.model.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

//HomeFragment hereda de Fragment, de esta manera se implementan las tres interfaces
class HomeFragment : Fragment(), AddTodoPopupFragment.DialogNextBtnClickListener,
    ToDoAdapter.ToDoAdapterClickInterface {

    //Para poder traer todos los datos desde firebase
    private lateinit var auth : FirebaseAuth
    //DataaseReference es un obejeto de Navcontroller, junto con navController
    private lateinit var databaseRef: DatabaseReference
    private  lateinit var  navController: NavController
    //Binding que en enlaza los datos otrogados por el usuario con la vista
    private lateinit var binding: FragmentHomeBinding
    private var  popupFragment: AddTodoPopupFragment? = null
    private lateinit var adapter : ToDoAdapter
    //Esta es una lista mutable de los objetos de la clase ToDoData
    private lateinit var mList: MutableList<ToDoData>


    //El método onCreateView se encarga de inflar el diseño de la interfaz de usuario
    // (UI) para este fragmento utilizando el objeto binding y devuelve la raíz de la vista inflada.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //En este método, se inicializan algunos de los atributos privados utilizando el método init
        init(view)
        //Se obtienen los datos de la base de datos de Firebase utilizando el método
        getDataFromFireBase()
        //En este metodo se registran todos los eventos
        registerEvents()
    }


    //Este metodo se encarga de registrar el evento de click del botón "addTaskBtn".
    private fun registerEvents(){
        binding.addTaskBtn.setOnClickListener{
            //Si el objeto popupFragment no es nulo, se elimina.
            if(popupFragment != null)
                childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
            popupFragment = AddTodoPopupFragment()
            popupFragment!!.setListener(this)
            popupFragment!!.show(
                childFragmentManager,
                AddTodoPopupFragment.TAG
            )
        }
    }

    private fun init(view : View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())

        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ToDoAdapter(mList)
        adapter.setListener(this)
        binding.mainRecyclerView.adapter = adapter
    }


    //Si se detecta algún cambio en los datos utilizando el listener ValueEventListener,
    // se borran todos los elementos de la lista mList y se agregan los nuevos datos a la lista.
    private fun getDataFromFireBase(){
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children){
                    val todoTask = taskSnapshot.key?.let {
                        ToDoData(it, taskSnapshot.value.toString())
                    }
                    if(todoTask != null){
                        mList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    //Todos los metodos instanciados en este bloque pertenecen a las interfaces

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {

        databaseRef.push().setValue(todo).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "Task saved", Toast.LENGTH_SHORT).show()
                todoEt.text = null
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            popupFragment!!.dismiss()
        }
    }

    //stos métodos se utilizan para guardar, actualizar, eliminar y editar tareas utilizando
    // la base de datos de Firebase.

    override fun onUpdateSaveTask(toDoData: ToDoData, todoEt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        databaseRef.updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null

            popupFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(toDoData: ToDoData) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskBtnClicked(toDoData: ToDoData) {
        if(popupFragment!= null)
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
        popupFragment = AddTodoPopupFragment.newInstance(toDoData.taskId, toDoData.task)
        popupFragment!!.setListener(this)
        popupFragment!!.show(childFragmentManager, AddTodoPopupFragment.TAG)
    }

}