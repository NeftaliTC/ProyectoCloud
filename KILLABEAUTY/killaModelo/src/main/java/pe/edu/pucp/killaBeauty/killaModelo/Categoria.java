package pe.edu.pucp.killaBeauty.killaModelo;

public class Categoria {
    private int id;
    private String descripcion;
	private Boolean activo;

	public Categoria() {
	}

	public Categoria(int id, String descripcion, Boolean activo) {
		this.id = id;
		this.descripcion = descripcion;
		this.activo = activo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}
