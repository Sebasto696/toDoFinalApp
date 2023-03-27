package com.example.myapplication.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.EachTodoItemBinding
import com.example.myapplication.utils.model.ToDoData

//Este bloque de codigo empieza con la definición de la clase ToDoAdapter que extiende la clase RecylcerView.Adapter
//Se utiliza para mostrar la lista de tareas que tiene el usuario (A nivel de interfaz)

class ToDoAdapter(private val list:MutableList<ToDoData>) :

    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

    /*Aqui se define una una interfaz para manejar el click de cada botón que se encuentre en la interfaz
    Esta interfaz tiene dos metodos:

    onDeleteTaskBtnClicked()
    onEditTaskBtnClicked()

    Estos dos metodos toman un objeto ToDoData como parametro
     */
    private var listener:ToDoAdapterClickInterface? = null

    /*
    El método setListener() se utiliza para establecer un objeto de la interfaz ToDoAdapterClickInterface
    como listener para manejar los clicks en los botones de la interfaz de usuario.
     */
    fun setListener(listener:ToDoAdapterClickInterface){
        this.listener = listener
    }

    inner class ToDoViewHolder(val binding:EachTodoItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent , false)
        return ToDoViewHolder(binding)
    }

    //El método getItemCount() devuelve el número de elementos en la lista.
    override fun getItemCount(): Int {
        return list.size
    }

    //El método onBindViewHolder() se utiliza para enlazar los datos en cada objeto ToDoData con la vista correspondiente en la interfaz de usuario.
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text = this.task

                binding.deleteTask.setOnClickListener{

                    listener?.onDeleteTaskBtnClicked(this)
                }
                binding.editTask.setOnClickListener{
                    listener?.onEditTaskBtnClicked(this)
                }
            }
        }
    }

    interface ToDoAdapterClickInterface{
        fun onDeleteTaskBtnClicked(toDoData : ToDoData)
        fun onEditTaskBtnClicked(toDoData: ToDoData)
    }
}
