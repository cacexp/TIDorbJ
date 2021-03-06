/*
* MORFEO Project
* http://www.morfeo-project.org
*
* Component: TIDorbJ
* Programming Language: Java
*
* File: $Source$
* Version: $Revision: 2 $
* Date: $Date: 2005-12-19 08:58:21 +0100 (Mon, 19 Dec 2005) $
* Last modified by: $Author: caceres $
*
* (C) Copyright 2004 Telefónica Investigación y Desarrollo
*     S.A.Unipersonal (Telefónica I+D)
*
* Info about members and contributors of the MORFEO project
* is available at:
*
*   http://www.morfeo-project.org/TIDorbJ/CREDITS
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under GPL, you probably need to
* purchase a commercial license of the product.  More info about
* licensing options is available at:
*
*   http://www.morfeo-project.org/TIDorbJ/Licensing
*/    
package es.tid.TIDorbj.core.cdr;

/**
 * Represents the contexts of a buffer marshaling across different
 * encapsulations. The context is defined by:
 * <ul>
 * <li>if it is a encapsulation context
 * <li>the alignment offset
 * <li>the byte-order
 * <li>the father context
 * </ul>
 * The context creates a list of encapsulations for navigate in and out of
 * encapsulations. The list represented by the context is a
 *
 * @see TIDorb.corba.cdr.AlignmentOffset
 * 
 * @autor Juan A. C&aacute;ceres
 * @version 1.1
 */

public class ContextCDR
{

    /**
     * big-endian if <code>true</code> or little-endian, otherwise.
     */
    protected boolean m_byte_order;

    /**
     * The father context. In the root context, the father will be
     * <code>null</code>.
     */

    protected ContextCDR m_father;

    /**
     * alignment offset of the encapsulation.
     * 
     * @see es.tid.TIDorbj.core.cdr.AlignmentOffset
     */
    protected AlignmentOffset m_offset;

    /**
     * Where is the beginnig of the encapsulation.
     */

    protected PointerCDR m_starts_at;

    /**
     * The indirection node of the buffer. Here are all the typecodes that have
     * been readed and can be indirectioned.
     */

    private IndirectionNode m_indirection_node;

    /**
     * The TypeCode Position Node of the buffer. Here are all the typecodes that
     * have been writed in the buffer and can be indirectioned.
     */

    private PositionsNode m_positions_node;

    /**
     * The CDROutputStream that will write the encapsulation length.
     */

    protected CDROutputStream m_length_out;

    /**
     * Constructor of the root context. A route of encapsulation contexts will
     * end here.
     * 
     * @see es.tid.TIDorbj.core.cdr.AlignmentOffset
     */

    public ContextCDR(PointerCDR start)
    {
        m_byte_order = CDR.LOCAL_BYTE_ORDER;
        m_offset = AlignmentOffset.calculateOffsetFrom(0);
        m_father = null;
        m_starts_at = start;
        m_indirection_node = null;
        m_positions_node = null;
    }

    public ContextCDR(PointerCDR start, ContextCDR father,
                      AlignmentOffset offset)
    {
        m_starts_at = start;
        m_byte_order = father.m_byte_order;
        this.m_offset = offset;
        this.m_father = father;
        m_indirection_node = null;
        m_positions_node = null;
    }

    public ContextCDR getFather()
    {
        return m_father;
    }

    public boolean isRootContext()
    {
        return m_father == null;
    }

    public void setOffset(AlignmentOffset value)
    {
        m_offset = value;
    }

    public AlignmentOffset getOffset()
    {
        return m_offset;
    }

    public void setByteOrder(boolean value)
    {
        m_byte_order = value;
    }

    public boolean getByteOrder()
    {
        return m_byte_order;
    }

    public void setLengthOut(CDROutputStream out)
    {
        m_length_out = out;
    }

    public void deleteLengthOut()
    {
        m_length_out = null;
    }

    public PointerCDR getStartPointer()
    {
        return m_starts_at;
    }

    public synchronized PointerCDR lookupPosition(java.lang.Object id)
    {
        PositionsNode node = getPositionsNode();

        PointerCDR position = node.lookup(id);

        if (position != null)
            return position;

        if (m_father == null)
            return null;

        position = m_father.lookupPosition(id);

        if (position != null)
            node.indirection(position.getAbsolutePosition());

        return position;

    }

    public synchronized void putObject(java.lang.Object obj, 
                                       PointerCDR position)
    {
        getPositionsNode().put(obj, position);
    }

    public synchronized Object lookupObject(AbsolutePosition position)
    {
        IndirectionNode node = getIndirectionNode();

        Object obj = node.lookup(position);

        if (obj != null)
            return obj;

        if (m_father != null)
            return m_father.lookupObject(position);
        else
            return null;

    }

    public synchronized void putPosition(AbsolutePosition position,
                                         java.lang.Object obj)
    {
        getIndirectionNode().put(position, obj);
    }

    private synchronized IndirectionNode getIndirectionNode()
    {
        if (m_indirection_node == null) {
            AbsolutePosition starting = m_starts_at.getAbsolutePosition();
            if (m_father == null)
                m_indirection_node = new IndirectionNode(starting);
            else
                m_indirection_node =
                    new IndirectionNode(starting,
                                        m_father
                                        .getIndirectionNode()
                                        .getTable());
        }

        return m_indirection_node;
    }

    private synchronized PositionsNode getPositionsNode()
    {
        if (m_positions_node == null) {
            m_positions_node = 
                new PositionsNode(m_starts_at.getAbsolutePosition());
        }

        return m_positions_node;
    }

    public boolean inAnEncapsulation()
    {
        return (m_father != null);
    }

    public boolean hasExternalIndirections()
    {
        return (((m_indirection_node == null) ? false
            : m_indirection_node.hasExternalIndirections()) 
            || ((m_positions_node == null) ? false
            : m_positions_node.hasExternalIndirections()));
    }
}

