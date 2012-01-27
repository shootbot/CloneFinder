import java.util.*;

public class Node {
    public NodeType type; // folder or file
    public Uniqueness uniqns; //
    public String name;
    public Node parent;
	public String hash;
	
        public ArrayList<Node> children;

        public Node(NodeType t, String n)
        {
            type = t;
            name = n;
            children = new ArrayList<Node>();
        }

        public void addChild(Node c)
        {
            children.add(c);
            c.parent = this;
        }

        /*public Node copy(Node p)
        {
            Node NodeCopy = new Node(this.type, this.name);
            for (Node c : this.children)
            {
                c.copy(NodeCopy);
            }
            return NodeCopy;
        }*/
		
        public void delchild(Node n)
        {
            Node p = n.parent;
            if (p.children.contains(n))
            {
                p.children.remove(n);
            }
            else
            {
                System.out.println("error: attempt to remove nonexistent child");
            }
        }

        public void show(int depth) {
            for (int j = 0; j < depth; j++) {
                System.out.print("  ");
            }
            System.out.println(this.name + " " + this.hash);
			

            for (Node c : this.children) {
                c.show(depth + 1);
            }
        }

        /*public void writehtml(StreamWriter treesw, int depth = 0)
        {
            if (depth == 0)
            {
                treesw.WriteLine("<html><head><style type=\"text/css\">");
                treesw.WriteLine("p.b {color:blue; line-height:0%;font-family:Lucida Console;}");
                treesw.WriteLine("p.g {color:green; line-height:0%;font-family:Lucida Console;}");
                treesw.WriteLine("p.r {color:red; line-height:0%;font-family:Lucida Console;}");
                treesw.WriteLine("p.v {color:violet; line-height:0%;font-family:Lucida Console;}");
                treesw.WriteLine("</style></head><body>");
            }

            if (type == NodeType.FOLDER)
            {
                treesw.Write("<p class=\"b\">");// folder
            }
            else
            {
                treesw.Write("<p class=\"g\">");// file
            }

            for (int j = 0; j < depth; j++)
            {
                treesw.Write("&nbsp;|&nbsp;");
            }
            treesw.WriteLine(this.name + "</p>");

            foreach (Node c in this.children)
            {
                c.writehtml(treesw, depth + 1);
            }
            if (depth == 0)
            {
                treesw.WriteLine("</body></html>");
            }
        }*/

        /*public void writexml(StreamWriter treesw, int depth = 0)
        {
            if (depth == 0)
            {
                treesw.WriteLine("<?xml version='1.0' encoding='UTF-8'?>");
                treesw.WriteLine("<!DOCTYPE xml>");
            }

            if (type == NodeType.FOLDER)
            {
                for (int j = 0; j < depth; j++) treesw.Write("  ");
                treesw.WriteLine("<" + this.name.Replace(' ', '_').Replace('(', '_').Replace(')', '_') + ">");

                foreach (Node c in this.children)
                {
                    c.writexml(treesw, depth + 1);
                }

                for (int j = 0; j < depth; j++) treesw.Write("  ");
                treesw.WriteLine("</" + this.name.Replace(' ', '_').Replace('(', '_').Replace(')', '_') + ">");
            }
            else
            {
                for (int j = 0; j < depth; j++) treesw.Write("  ");
                treesw.WriteLine("<" + this.name.Replace(' ', '_').Replace('(', '_').Replace(')', '_') + "/>");
            }
        }*/
    }