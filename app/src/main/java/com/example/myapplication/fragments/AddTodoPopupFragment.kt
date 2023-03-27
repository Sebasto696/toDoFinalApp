package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddTodoPopupBinding
import com.example.myapplication.utils.model.ToDoData
import com.google.android.material.textfield.TextInputEditText


class AddTodoPopupFragment : DialogFragment() {
    
    //Hace referencia a la clase de unión que se utiliza para unir vistas y datos en la IU.
    private lateinit var binding : FragmentAddTodoPopupBinding
    //Define los métodos onSaveTask y onUpdateSaveTask, los cuales se utilizan para guardar y actualizar una tarea.
    private lateinit var listener : DialogNextBtnClickListener
    //Contiene datos de tarea.
    private var toDoData : ToDoData? = null

    fun setListener(listener : DialogNextBtnClickListener){
        this.listener = listener
    }

    companion object{
        //TAG: es util para identificar la instancia de la clase.
        const val TAG = "AddTodoPopUpFragment"
        @JvmStatic
        //Esta información estatica define los argumentos de la tarea
        fun newInstance(taskId:String, task:String) = AddTodoPopupFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentAddTodoPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(arguments != null){
            toDoData = ToDoData(arguments?.getString("taskId").toString()
                , arguments?.getString("task").toString()
            )
            binding.todoEt.setText(toDoData?.task)

        }
        registerEvents()
    }

    private fun registerEvents(){
        binding.todoNextBtn.setOnClickListener{
                val todoTask = binding.todoEt.text.toString()
            if (todoTask.isNotEmpty()){
                if(toDoData == null){
                    listener.onSaveTask(todoTask, binding.todoEt)
                }else{
                    toDoData?.task = todoTask
                    listener.onUpdateSaveTask(toDoData!!, binding.todoEt)
                }
            }else{
                Toast.makeText(context, "Please type some task", Toast.LENGTH_SHORT).show()
            }

        }

        binding.todoClose.setOnClickListener{
            dismiss()
        }
    }

    interface DialogNextBtnClickListener{
        fun onSaveTask(todo : String, todoEt : TextInputEditText)
        fun onUpdateSaveTask(toDoData: ToDoData, todoEt : TextInputEditText)
    }

}