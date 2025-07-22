import java.io.*;
import java.util.*;

public class Biblioteca {
    public static void main(String[] args) {
        String archivoEntrada = "prestamos.txt";
        String archivoSalida = "reporte.txt";

        Map<String, List<String>> prestamos = new TreeMap<>();
        
        // Leer archivo de entrada
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoEntrada))){
            String lineaLeer;
            
            if((lineaLeer = reader.readLine()) == null){
                return;
            }
            
            reader.close();
            
            while((lineaLeer = reader.readLine()) != null){
                String[] partes = lineaLeer.split(",");
                if(partes.length != 2 || partes[0].trim().isEmpty() || partes[1].trim().isEmpty()){
                    continue;
                }
            
                String usuario = partes[0];
                String libro = partes[1];
                
                prestamos.putIfAbsent(usuario, new ArrayList<>());
                prestamos.get(usuario).add(libro);
                
            } 
            
        }catch(IOException e){
                return;
            }
            
        
        // Escribir archivo de salida
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))){
            for(Map.Entry<String, List<String>> entry : prestamos.entrySet()){
                String usuario = entry.getKey();
                List<String> libros = entry.getValue();
                
                writer.write(usuario + ":");
                writer.newLine();
                
                for(String libro : libros){
                    writer.write("\t- " + libro);
                    writer.newLine();
                }
                
                writer.newLine();
            }
        } catch(IOException e){
            System.out.println("Error al escribir el archivo");
        }
    }
}
