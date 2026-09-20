package pe.edu.pucp.killaDAO;

public class MainDemoCRUD {

    public static void main(String[] args) {
        System.out.println("=== INICIO DEMO CRUD ===");

        runTest("Categoria", () -> TestCategoriaDAO.main(new String[]{}));
        runTest("Marca", () -> TestMarcaDAO.main(new String[]{}));
        runTest("Permiso", () -> TestPermisoDAO.main(new String[]{}));
        runTest("Subcategoria", () -> TestSubCategoria.main(new String[]{}));

        System.out.println("=== FIN DEMO CRUD ===");
    }

    private static void runTest(String nombre, Runnable test) {
        System.out.println("\n--- TEST " + nombre.toUpperCase() + " ---");
        try {
            test.run();
            System.out.println("RESULTADO " + nombre + ": OK");
        } catch (Exception e) {
            System.out.println("RESULTADO " + nombre + ": ERROR");
            e.printStackTrace();
        }
    }
}